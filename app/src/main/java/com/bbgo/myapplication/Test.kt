package com.bbgo.myapplication

import org.bouncycastle.util.encoders.Hex
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet


/**
 *  author: wangyb
 *  date: 2021/6/28 10:46 上午
 *  description: todo
 */
class Test {


}

fun main(args: Array<String>) {

    println("Hello World = ")
    println(Hex.toHexString("012345678910111213141516171819202122232425262728293031".hashCode().toString().toByteArray()))
    val array = twoNumSum(intArrayOf(2, 7, 11, 15), 9)
    array.forEach {
        println(it)
    }

    val array2 = twoNumSum(intArrayOf(2, 7, 11, 15), 9)
    array2.forEach {
        println(it)
    }

    val node1 = ListNode()
    val node2 = ListNode()
    val node3 = ListNode()
    node1.value = 2
    node2.value = 3
    node3.value = 4
    node1.nextNode = node2
    node2.nextNode = node3
    node3.nextNode = null

    val node4 = ListNode()
    val node5 = ListNode()
    val node6 = ListNode()
    node4.value = 1
    node5.value = 3
    node6.value = 5
    node4.nextNode = node5
    node5.nextNode = node6
    node6.nextNode = null

    var result = addTwoNumbers(node1, node4)

    while (result != null) {
        println(result.value)
        result = result.nextNode
    }

    subStringLength("pwwkew")

    println("length = ${removeDuplicates(intArrayOf(0, 0, 1, 1, 2, 2, 3, 3, 4))}")

    mergeTwoList(node1, node4)
//    removeElement(intArrayOf(0, 0, 1, 1, 2, 2, 3, 3, 4), 2)



    val rootNode = TreeNode(1)
    rootNode.left = TreeNode(5)
    rootNode.right = TreeNode(2)
    rootNode.right?.left = TreeNode(3)
    rootNode.right?.right = TreeNode(4)

    Solution().inorderTraversal(rootNode)

    println(maxDepth(rootNode))

    println("hasCycle = ${hasCycle()}")

}

/**
 * 两数之和 时间复杂度O(N2）
 */
fun twoNumSum(nums: IntArray, target: Int): Array<Int> {
    var intK = 0
    var intJ = 0
    for (indexI in nums.indices) {
        intK = indexI
        val num = target - nums[indexI]
        for (indexJ in nums.indices) {
            if (num == nums[indexJ]) {
                intJ = indexJ
                break
            }
        }
        if (intJ != 0) {
            break
        }
    }
    return arrayOf(intK, intJ)
}

/**
 * 时间复杂度 O(N)
 */
fun twoNumSum2(nums: IntArray, target: Int): Array<Int> {
    val hashMap = HashMap<Int, Int>()
    for (index in nums.indices) {
        if (hashMap.containsKey(target - nums[index])) {
            return arrayOf(hashMap[target - nums[index]]!!, index)
        }
        hashMap[nums[index]] = index
    }
    return arrayOf()
}

class ListNode(var value: Int = 0) {
    var nextNode: ListNode? = null
}

/**
 * 两数相加 时间复杂度 O(max(m,n))
 */
fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
    var l1 = l1
    var l2 = l2
    var head: ListNode? = null
    var tail: ListNode? = null
    var carry = 0
    while (l1 != null || l2 != null) {
        val n1 = l1?.value ?: 0
        val n2 = l2?.value ?: 0
        val sum = n1 + n2 + carry
        if (head == null) { // 构造头指针和尾指针
            head = ListNode(sum % 10)
            tail = head
        } else {
            tail?.nextNode = ListNode(sum % 10) // 构造新的节点，并将tail的next指向该节点
            tail = tail?.nextNode  // 移动tail指针，指向新节点
        }
        carry = sum / 10
        /**
         * 移动指针，指向下一个节点
         */
        l1 = l1?.nextNode
        l2 = l2?.nextNode
    }
    if (carry > 0) {
        tail?.nextNode = ListNode(carry)
    }
    return head
}

fun subStringLength(value: String) {
    var start = 0
    var end = 0
    var count = 0
    var resultString = ""
    val list = arrayListOf<String>()
    for (index in 1..value.length) {
        if (start > value.length || index > value.length) {
            break
        }
        if (index + 1 > value.length) {
            break
        }
        if (value.substring(start, 1) == value.substring(index, index + 1)) {
            list.add(value.substring(start, index))
            start += 1
        } else {
            end = index
        }
    }

    list.forEach {
        if (it.length > count) {
            count = it.length
            resultString = it
            return@forEach
        }
    }

    println(resultString)
}

/**
 * 给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。
    不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成
 */
fun removeDuplicates(nums: IntArray): Int {
    val n = nums.size
    if (n == 0) {
        return 0
    }
    var fast = 1
    var slow = 1
    while (fast < n) {
        if (nums[fast] != nums[fast - 1]) {
            nums[slow] = nums[fast]
            ++slow
        }
        ++fast
    }
    nums.forEach {
        print("$it ")
    }
    return slow
}

/**
 * 合并两个有序链表
 * 时间复杂度O(n+m)
 */
fun mergeTwoList(l1: ListNode?, l2: ListNode?) {
    var node1 = l1
    var node2 = l2
    val prevHead = ListNode(-1)
    var head: ListNode? = prevHead

    while (node1 != null && node2 != null) {
        if (node1.value <= node2.value) {
            head?.nextNode = node1
            node1 = node1.nextNode
        } else {
            head?.nextNode = node2
            node2 = node2.nextNode
        }
        head = head?.nextNode
    }
    if (node1 == null) {
        head?.nextNode = node2
    } else {
        head?.nextNode = node1
    }

    println(prevHead.nextNode)
}

/**
 * 爬楼梯
 * 时间复杂度O(n)
 */
fun climbStairs(n: Int): Int {
    var p = 0
    var q = 0
    var result = 1
    var i = 1
    while (i <= n) {
        p = q
        q = result
        result = p + q
        i++
    }
    return result
}

class TreeNode(var value: Int = 0) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}

class Solution {

    /**
     * 时间复杂度均为O(n) n是代表有多少个节点
     */
    fun inorderTraversal(root: TreeNode?): List<Int> {
        val res: MutableList<Int> = ArrayList()

        beforOrder(root, res)
        println(res)

        res.clear()
        inorder(root, res)
        println(res)

        res.clear()
        afterOrder(root, res)
        println(res)
        return res
    }

    /**
     * 前序遍历  根->左->右
     */
    fun beforOrder(root: TreeNode?, res: MutableList<Int>) {
        if (root == null) {
            return
        }
        res.add(root.value)
        beforOrder(root.left, res)
        beforOrder(root.right, res)
    }

    /**
     * 中序遍历  左—>根—>右
     */
    fun inorder(root: TreeNode?, res: MutableList<Int>) {
        if (root == null) {
            return
        }
        inorder(root.left, res)
        res.add(root.value)
        inorder(root.right, res)
    }

    /**
     * 后续遍历  左->右->根
     */
    fun afterOrder(root: TreeNode?, res: MutableList<Int>) {
        if (root == null) {
            return
        }
        afterOrder(root.left, res)
        afterOrder(root.right, res)
        res.add(root.value)
    }
}

/**
 * 二叉树最大深度 O(n)
 */
fun maxDepth(root: TreeNode?): Int {
    return if (root == null) {
        0
    } else {
        val leftHeight = maxDepth(root.left)
        val rightHeight = maxDepth(root.right)
        Math.max(leftHeight, rightHeight) + 1
    }
}

/**
 * 判断一个链表是否有环
 */
fun hasCycle(): Boolean {
    var root: ListNode? = ListNode()
    val node1 = ListNode()
    val node2 = ListNode()
    val node3 = ListNode()
    root?.value = 1
    node1.value = 2
    node2.value = 3
    node3.value = 4
    root?.nextNode = node1
    node1.nextNode = node2
    node2.nextNode = node3
    node3.nextNode = null
//    node3.nextNode = node1

    val set = HashSet<ListNode>()
    while (root != null) {
        if (!set.add(root)) {
            return true
        }
        root = root.nextNode
    }
    return false
}



